package br.com.mcm.apimcmfood.domain.exception.handler;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ErroPadrao(
        Integer status,
        String type,
        String title,
        String detail,
        List<Field> fields
) {
    public ErroPadrao(
            final Integer status,
            final String title
    ){
        this(status, title, null,null,null);
    }

    public ErroPadrao(
            final Integer status,
            final String title,
            final String detail
    ){
        this(status, title, detail,null,null);
    }
}
