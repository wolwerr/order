package org.test.order.domain.generic.output;

import lombok.Getter;
import org.test.order.domain.entity.OrderEntity;

@Getter
public class OutputError implements OutputInterface{
    public final String mensagem;
    public final OutputStatus outputStatus;
    public OutputError (String mensagem, OutputStatus outputStatus){
        this.mensagem = mensagem;
        this.outputStatus = outputStatus;

    }
    @Override
    public Object getBody() {
        return outputStatus;
    }
}
