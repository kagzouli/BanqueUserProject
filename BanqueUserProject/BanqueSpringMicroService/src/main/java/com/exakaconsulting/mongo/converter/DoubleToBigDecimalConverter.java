package com.exakaconsulting.mongo.converter;

import java.math.BigDecimal;

import org.springframework.core.convert.converter.Converter;

public class DoubleToBigDecimalConverter implements Converter<Double, BigDecimal> {
 
    @Override
    public BigDecimal convert(Double source) {
    	BigDecimal value = new BigDecimal(source);
        return value;
    }

}
