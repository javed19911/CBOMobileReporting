package utils_new;

public class CustomError {

    public enum Code{
        ok,
        cancel,
        error
    }

    private Code _code = Code.error;
    private String Description = "";
    private String ErrorMessage = "";



    public Code getCode() {
        return _code;
    }

    public CustomError setCode(Code _code) {
        this._code = _code;
        return this;
    }

    public String getDescription() {
        return Description;
    }

    public CustomError setDescription(String description) {
        Description = description;
        return this;
    }

    public String getErrorMessage() {
        return ErrorMessage;
    }

    public CustomError setErrorMessage(String errorMessage) {
        ErrorMessage = errorMessage;
        return this;
    }
}
