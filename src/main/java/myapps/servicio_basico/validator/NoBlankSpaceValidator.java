package myapps.servicio_basico.validator;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import java.util.Optional;

@FacesValidator("NoBlankSpaceValidator")
public class NoBlankSpaceValidator implements Validator {
    private static final String NO_BLANK_SPACE = "NO_BLANK_SPACE";
    private static final String REGEX_SUMMARY = "REGEX_SUMMARY";

    @Override
    public void validate(FacesContext facesContext, UIComponent uiComponent, Object value) {
        String message = Optional.ofNullable((String) uiComponent.getAttributes().get(NO_BLANK_SPACE)).orElse("");
        String sumary = Optional.ofNullable((String) uiComponent.getAttributes().get(REGEX_SUMMARY)).orElse("");

        Optional<String> optValue = Optional.ofNullable((String) value);
        if (optValue.isPresent()) {
            if (optValue.get().trim().isEmpty()) {
                FacesMessage msg = new FacesMessage(null, message);
                msg.setSummary(sumary);
                msg.setSeverity(FacesMessage.SEVERITY_ERROR);
                throw new ValidatorException(msg);
            }
        } else {
            FacesMessage msg = new FacesMessage(null, message);
            msg.setSeverity(FacesMessage.SEVERITY_ERROR);
            throw new ValidatorException(msg);
        }

    }
}
