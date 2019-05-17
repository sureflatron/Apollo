package myapps.servicio_basico.validator;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@FacesValidator("regexResolverValidator")
public class RegexResolverValidator implements Validator {
    private static final String REGEX_VALIDATOR = "REGEX_VALIDATOR";
    private static final String REGEX_MESSAGE = "REGEX_MESSAGE";
    private static final String REGEX_SUMMARY = "REGEX_SUMMARY";
    private static final String FIELD_VALUE = "FIELD_VALUE";

    @Override
    public void validate(FacesContext facesContext, UIComponent uiComponent, Object value) {
        String regexMsg = Optional.ofNullable((String) uiComponent.getAttributes().get(REGEX_MESSAGE)).orElse("");
        String regex = Optional.ofNullable((String) uiComponent.getAttributes().get(REGEX_VALIDATOR)).orElse("");
        String sumary = Optional.ofNullable((String) uiComponent.getAttributes().get(REGEX_SUMMARY)).orElse("");
        String field = Optional.ofNullable((String) uiComponent.getAttributes().get(FIELD_VALUE)).orElse("");
        regexMsg = regexMsg.replace("%", field);

        String valor = (String) value;
        if (!valor.isEmpty()) {
            Optional<String> optValue = Optional.ofNullable(valor);
            Pattern mask = Pattern.compile(regex);

            if (optValue.isPresent()) {
                Matcher matcher = mask.matcher(optValue.get());
                if (!matcher.matches()) {
                    FacesMessage facesMessage = new FacesMessage();
                    facesMessage.setSummary(sumary);
                    facesMessage.setDetail(regexMsg);
                    facesMessage.setSeverity(FacesMessage.SEVERITY_WARN);
                    throw new ValidatorException(facesMessage);
                }
            }
        }
    }
}

