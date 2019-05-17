package myapps.servicio_basico.util;/*package com.myapps.servicio_basico.util;

import java.beans.PersistenceDelegate;
import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;

import org.apache.log4j.Logger;

public class ToXml implements Serializable {

	private static final long serialVersionUID = 1L;
	public static Logger log = Logger.getLogger(ToXml.class);

	public <T> void writeObject(ArrayList<T> list, String fileName) throws Exception {
		XMLEncoder encoder = null;
		try {
			encoder = new XMLEncoder(new BufferedOutputStream(new FileOutputStream(fileName)));
			encoder.writeObject(list);			
		} catch (Exception e) {
			throw new Exception(e.getMessage(), e);
		} finally {
			if (encoder != null) {
				encoder.close();
			}
		}

	}

	public <T> void write(T object, String fileName) {
		XMLEncoder encoder = null;
		try {
			encoder = new XMLEncoder(new BufferedOutputStream(new FileOutputStream(fileName)));
			encoder.writeObject(object);		
		} catch (Exception e) {
			e.getMessage();
		} finally {
			if (encoder != null) {
				encoder.close();
			}
		}

	}

	public String toXml(Object object) {
		String xml = "";
		ByteArrayOutputStream baos = null;
		XMLEncoder encoder = null;
		try {
			if (object != null) {
				baos = new ByteArrayOutputStream();
				encoder = new XMLEncoder(baos);

				PersistenceDelegate pd = encoder.getPersistenceDelegate(Integer.class);
				encoder.setPersistenceDelegate(BigDecimal.class, pd);
				encoder.setPersistenceDelegate(java.math.BigInteger.class, pd);

				encoder.writeObject(object);
				encoder.close();
				encoder = null;

				xml = baos.toString("UTF-8");
			}

		} catch (Exception e) {
			e.getMessage();
		} finally {
			if (encoder != null) {
				try {
					encoder.close();
				} catch (Exception e) {
					e.getMessage();
				}
			}
			if (baos != null) {
				try {
					baos.close();
				} catch (Exception e) {
					e.getMessage();
				}
			}
		}
		return xml;
	}

	@SuppressWarnings("unchecked")
	public <T> ArrayList<T> readObject(String fileName) {
		XMLDecoder decoder = null;
		// InputStream resourceAsStream = null;
		FileInputStream is = null;
		try {
			is = new FileInputStream(fileName);
		
			decoder = new XMLDecoder(new BufferedInputStream(is));
			return (ArrayList<T>) decoder.readObject();

		} catch (Exception e) {
			log.error("Error al cargar objeto xml: ", e);			
		} finally {
			if (decoder != null) {
				try {
					decoder.close();
				} catch (Exception e) {
					e.getMessage();
				}
			}
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					e.getMessage();
				}
			}
		}
		return new ArrayList<T>();
	}
	
	public Object xmlToObject(String xml) {
		XMLDecoder decoder = null;
		InputStream resourceAsStream = null;
		try {

			resourceAsStream = new ByteArrayInputStream(xml.getBytes("UTF-8"));
			decoder = new XMLDecoder(new BufferedInputStream(resourceAsStream));
			Object readObject = decoder.readObject();
			decoder.close();
			decoder = null;			
			return readObject;

		} catch (Exception e) {
			log.error("Error al cargar objeto xml: ", e);
		} finally {
			if (decoder != null) {
				try {
					decoder.close();
				} catch (Exception e) {
					e.getMessage();
				}
			}
			if (resourceAsStream != null) {
				try {
					resourceAsStream.close();
				} catch (IOException e) {
					e.getMessage();
				}
			}
		}
		return new Object();
	}

	public static void main(String[] args) {
		try {

			// ArrayList<StatusDetail> lista = new ArrayList<>();
			// lista.add(new StatusDetail("00","AC (activo)"));
			// lista.add(new StatusDetail("20","Suspendido/MO"));
			// lista.add(new StatusDetail("02","Robo/Fraude"));
			// lista.add(new StatusDetail("22","Suspendido/MO/Robo/Fraude"));
			// ToXml main = new ToXml();
			// System.out.println(main.toXml(lista));

			// ArrayList<Currency> listAcc = new ArrayList<Currency>();
			// listAcc.add(new Currency(1000, "AED", "United Arab Emirates Dirham"));
			// listAcc.add(new Currency(1001, "AFN", "Afghan Afghani"));
			// listAcc.add(new Currency(1002, "ALL", "Albanian Lek"));
			// ToXml main = new ToXml();
			// System.out.println("to xml: " + main.toXml(listAcc));

			// System.out.println(":" + main.toXml(listAcc) + ":");
			// main.writeObject(listAcc, "E:/");
			// ArrayList<Currency> list = main.readObject("E:\\currency.xml");
			// System.out.println(list.toString());

			// ArrayList<FreeUnitMeasure> list = new ArrayList<FreeUnitMeasure>();
			// list.add(new FreeUnitMeasure("1003", "Second", 60, "Minute"));
			// list.add(new FreeUnitMeasure("1004", "Minute", 60, "Minute"));
			// list.add(new FreeUnitMeasure("1006", "Times", 60, "Times"));
			//
			// list.add(new FreeUnitMeasure("1101", "Item", 1, "Item"));
			// list.add(new FreeUnitMeasure("1106", "Bytes", 1024, "KB"));
			// list.add(new FreeUnitMeasure("1107", "KB", 1024, "MB"));
			// list.add(new FreeUnitMeasure("1108", "MB", 1, "MB"));
			// list.add(new FreeUnitMeasure("1109", "GB", 1, "MB"));
			// ToXml main = new ToXml();
			// main.writeObject(list, "E:/freeunit_measure.xml");

			String cad = "123456789";
			System.out.println(cad.substring(3 - 1, 3));

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
*/