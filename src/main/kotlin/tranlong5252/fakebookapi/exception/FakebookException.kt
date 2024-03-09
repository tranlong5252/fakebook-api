package tranlong5252.fakebookapi.exception

import tranlong5252.fakebookapi.exception.errors.ErrorReport

class FakebookException(val report : ErrorReport<*>) : Exception() {
}