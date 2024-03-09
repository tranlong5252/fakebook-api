package tranlong5252.fakebookapi.exception.errors

import tranlong5252.fakebookapi.exception.entity.ValidationError

class ValidationErrorReport(data: List<ValidationError>) : ErrorReport<List<ValidationError>>("Validation error", data)