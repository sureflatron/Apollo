package myapps.servicio_basico.util;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.sql.SQLException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class QueryBuilder {

	private static final Logger LOGGER = Logger.getLogger(QueryBuilder.class);
	private static final Pattern ALL_SPACES = Pattern.compile("\\s+");
	private static final Pattern SOME_PUNCTU = Pattern.compile(" ([,)])");

	private static final Pattern NAMED_PARAMETER_PATTERN = Pattern.compile(":[\\p{Alnum}_]+");

	public QueryBuilder() {
	}

	public static <T> List<T> query(String spec, final Map<String, Object> namedParameters, EntityManager em)
			throws SQLException {
		try {
			JSONObject jsonObject = new JSONObject(spec);
			return query(jsonObject, namedParameters, em);
		} catch (JSONException e) {
			throw new SQLException(e);
		}
	}

	/*
	 * private List<Map<String, Object>> query(String spec, Map<String, Object>
	 * namedParameters, Map<String, Object> extras, String
	 * selectClause,EntityManager em) throws SQLException { try { JSONObject
	 * jsonObject = new JSONObject(spec);
	 * 
	 * if (selectClause != null && !"select".equals(selectClause)) {
	 * jsonObject.put("select", jsonObject.get(selectClause)); }
	 * 
	 * return query(jsonObject, namedParameters, em); } catch (JSONException e) {
	 * throw new SQLException(e); } }
	 */

	@SuppressWarnings("unchecked")
	private static <T> List<T> query(JSONObject jsonObject, Map<String, Object> namedParameters, EntityManager em)
			throws JSONException, SQLException {
		Object[] objects = prepareQuery(namedParameters, jsonObject);
		String queryCrud = (String) objects[0];
		String queryConLimites = queryCrud;
		List<Object> prmLst = (List<Object>) objects[1];

		LOGGER.debug(
				"Se ha construido el siguiente query: NORMAL = {};" + queryConLimites + " PARAMETROS= {} " + prmLst);

		List<T> salida = executeNativeQuery(em, queryConLimites, prmLst);
		LOGGER.info("LISTA_QUERY " + salida.size());
		return salida;
	}

	@SuppressWarnings("unchecked")
	private static <T> List<T> executeNativeQuery(EntityManager em, String nativeQuery, List<Object> parameters) {
		Query query = em.createNativeQuery(nativeQuery);
		for (int i = 1; i <= parameters.size(); i++) {
			query.setParameter(i, parameters.get(i - 1));
		}
		return query.getResultList();
	}

	private static Object[] prepareQuery(final Map<String, Object> namedParameters, final JSONObject jsonObject)
			throws JSONException {
		StringBuffer stringbuffer = new StringBuffer(1000);
		List<Object> outParameters = new LinkedList<Object>();
		select(stringbuffer, jsonObject, namedParameters.keySet(), namedParameters, outParameters);
		String trimmed = stringbuffer.toString().trim();
		String withoutDuplicateSpaces = ALL_SPACES.matcher(trimmed).replaceAll(" ");
		String niceClosingOfParentesis = SOME_PUNCTU.matcher(withoutDuplicateSpaces).replaceAll("$1");
		return new Object[] { niceClosingOfParentesis, outParameters };
	}

	public static ArrayList<String> getCabecera(String spec) throws JSONException {
		ArrayList<String> r = new ArrayList<>();
		JSONObject jsonObject = new JSONObject(spec);
		JSONArray select = jsonObject.getJSONArray("select");
		if (select == null) {
			throw new IllegalArgumentException("Necesito select");
		}
		for (int i = 0; i < select.length(); i++) {
			String campo = select.getString(i);
			int pos = (campo.indexOf("as") > -1 ? campo.indexOf("as")
					: (campo.indexOf("AS") > -1 ? campo.indexOf("AS") : -1));
			if (pos > -1) {
				campo = campo.substring(pos + 3);
			}
			r.add(campo);
		}
		return r;
	}

	private static void select(StringBuffer appendable, JSONObject jsonObject, Set<String> keyParams,
			Map<String, Object> namedParameters, List<Object> outParameters) throws JSONException {

		Object select = jsonObject.opt("select");
		Object all = jsonObject.opt("all");
		Object distinct = jsonObject.opt("distinct");
		Object unique = jsonObject.opt("unique");
		Object from = jsonObject.opt("from");
		Object where = jsonObject.opt("where");
		Object groupBy = jsonObject.opt("groupBy");
		Object having = jsonObject.opt("having");
		Object union = jsonObject.opt("union");
		Object unionAll = jsonObject.opt("unionAll");
		Object orderBy = jsonObject.opt("orderBy");
		Object startWith = jsonObject.opt("startWith");
		Object connetBy = jsonObject.opt("connetBy");

		if (select == null) {
			throw new IllegalArgumentException("Necesito select");
		}
		appendable.append("select ");
		if (all != null && !"false".equals(all)) {
			appendable.append("all ");
		} else if (distinct != null && !"false".equals(distinct)) {
			appendable.append("distinct ");
		} else if (unique != null && !"false".equals(unique)) {
			appendable.append("unique ");
		}
		list(appendable, keyParams, namedParameters, select, outParameters);

		appendable.append("\nfrom ");
		list(appendable, keyParams, namedParameters, from, outParameters);
		if (where != null) {
			whereAndHavingClause("\nwhere (", appendable, keyParams, namedParameters, outParameters, where);
		}

		if (groupBy != null) {
			appendable.append("\ngroup by ");
			list(appendable, keyParams, namedParameters, groupBy, outParameters);
		}

		if (having != null) {
			whereAndHavingClause("\nhaving (", appendable, keyParams, namedParameters, outParameters, having);
		}

		if (union != null || unionAll != null) {
			if (unionAll != null) {
				if (union != null) {
					throw new IllegalArgumentException("Sólo debe existir un union o unionAll");
				}
				appendable.append("\nunion all (");
				union = unionAll;
			} else {
				appendable.append("\nunion (");
			}

			if (union instanceof JSONObject) {
				select(appendable, (JSONObject) union, keyParams, namedParameters, outParameters);
			} else {
				throw new IllegalArgumentException("Union debe ser un objeto select entero");
			}
			appendable.append(')');
			Object name = ((JSONObject) union).opt("name");
			if (name != null) {
				appendable.append(' ').append(name).append(' ');
			}
		}

		if (orderBy != null) {
			appendable.append("\norder by ");
			list(appendable, keyParams, namedParameters, orderBy, outParameters);
		}
		if (startWith != null) {
			appendable.append("\nstart with ");
			list(appendable, keyParams, namedParameters, startWith, outParameters);
		}
		if (connetBy != null) {
			appendable.append("\nconnect by ");
			list(appendable, keyParams, namedParameters, connetBy, outParameters);
		}
	}

	private static boolean whereAndHavingClause(String prefix, final StringBuffer appendable,
			final Set<String> keyParams, final Map<String, Object> namedParameters, final List<Object> outParameters,
			Object where) throws JSONException {
		if (where == null) {
			LOGGER.warn("where is null, No puede ser. Puede que haya un error en el query: prefix=" + prefix
					+ ". appendable= " + appendable + ". keyParams=" + keyParams + ". namedParams=" + namedParameters
					+ ". Out=" + outParameters + ". Where=" + where, new Throwable());
			return false;
		}

		if (where instanceof JSONObject) {
			appendable.append(prefix).append(((JSONObject) where).get("first")).append(" (");
			select(appendable, (JSONObject) where, keyParams, namedParameters, outParameters);
			appendable.append(" )) ");
			return false;
		} else if (where instanceof JSONArray) {
			JSONArray array = (JSONArray) where;
			for (int i = 0; i < array.length(); i++) {
				Object opt = array.opt(i);
				boolean b = whereAndHavingClause(prefix, appendable, keyParams, namedParameters, outParameters, opt);
				if (b) {
					prefix = " and (";
				}
			}
			if (" and (".equals(prefix)) {
				return true;
			}
		} else {
			return crearListaConParams(prefix, appendable, keyParams, namedParameters, outParameters, where);
		}

		return false;
	}

	private static boolean crearListaConParams(final String prefix, final StringBuffer appendable,
			final Collection<String> keyParams, final Map<String, Object> namedParameters,
			final Collection<Object> outParameters, final Object listaAProcesar) {
		final String obj = listaAProcesar.toString();
		Matcher matcher = NAMED_PARAMETER_PATTERN.matcher(obj);

		StringBuilder temporal = new StringBuilder(obj.length());
		Collection<String> keys = new LinkedList<String>();

		int start = 0;
		while (matcher.find(start)) {
			keys.add(matcher.group().substring(1));
			temporal.append(obj.substring(start, matcher.start())).append('?');
			start = matcher.end();
		}

		if (start == 0) {
			appendable.append(prefix).append(obj).append(')');
		} else {
			boolean all;
			if (keys.size() == 1) {
				all = Iterables.all(keys, new Predicate<String>() {

					@Override
					public boolean apply(String input) {
						return keyParams.contains(input);
					}
				});
			} else {
				all = true;
			}

			if (all) {
				appendable.append(prefix).append(temporal).append(obj.substring(start)).append(')');
				for (String key : keys) {
					Object e = namedParameters.get(key);
					if (e instanceof Date) {
						outParameters.add(new java.sql.Date(((Date) e).getTime()));
					} else {
						outParameters.add(e);
					}
				}
			} else {
				return false;
			}
		}

		return true;
	}

	private static void list(StringBuffer appendable, Set<String> keyParams, Map<String, Object> namedParameters,
			Object select, List<Object> outParameters) throws JSONException {
		if (select == null) {
			return;
		}

		if (select instanceof JSONObject) {
			appendable.append('(');
			JSONObject select1 = (JSONObject) select;
			select(appendable, select1, keyParams, namedParameters, outParameters);
			appendable.append(") ");
			Object name = select1.opt("name");
			if (name != null) {
				appendable.append(name).append(' ');
			}
		} else if (select instanceof JSONArray) {
			JSONArray array = (JSONArray) select;
			for (int i = 0; i < array.length(); i++) {
				list(appendable, keyParams, namedParameters, array.opt(i), outParameters);
				if (i < array.length() - 1) {
					appendable.append(", ");
				} else {
					appendable.append(' ');
				}
			}
		} else {
			final String obj = select.toString();
			final Matcher matcher = NAMED_PARAMETER_PATTERN.matcher(obj);
			final StringBuilder temporal = new StringBuilder(obj.length());

			int start = 0;
			while (matcher.find(start)) {
				String param = obj.substring(matcher.start() + 1, matcher.end());
				if (namedParameters.containsKey(param)) {
					outParameters.add(namedParameters.get(param));
				} else {
					outParameters.add(null);
				}
				temporal.append(obj.substring(start, matcher.start())).append('?');
				start = matcher.end();
			}

			if (start == 0) {
				appendable.append(obj).append(' ');
			} else {
				appendable.append(temporal.append(obj.substring(start))).append(' ');
			}
		}
	}

	/**
	 * Given a sql, make a window out of it.
	 *
	 * @param sql   the original SQL.
	 * @param first the first element to fetch.
	 * @param count the window size.
	 * @return a new sql sentence.
	 */
	/*
	 * private String limits(String sql, int first, int count) { int last = first +
	 * count; if (last < 0) { last = Integer.MAX_VALUE; }
	 * 
	 * if (first == 0 && last == Integer.MAX_VALUE) { return sql; }
	 * 
	 * if (first == 0) { return "select * from (" + sql + ") where rownum <= " +
	 * last; }
	 * 
	 * return "select * from (select * from (select a.*, rownum rnum from  (" + sql
	 * + ") a where ROWNUM <= " + last + ")) where rnum > " + first; }
	 */
}
