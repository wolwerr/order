package org.test.order.domain.generic.output;

import java.util.Objects;
import lombok.Getter;

@Getter
public class OutputStatus {
    private final int code;
    private final String codeName;
    private final String message;

    public OutputStatus(int code, String codeName, String message) {
        this.code = code;
        this.codeName = codeName;
        this.message = message;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OutputStatus that = (OutputStatus) o;
        return code == that.code &&
                Objects.equals(codeName, that.codeName) &&
                Objects.equals(message, that.message);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code, codeName, message);
    }

    @Override
    public String toString() {
        return "OutputStatus{" +
                "code=" + code +
                ", codeName='" + codeName + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
