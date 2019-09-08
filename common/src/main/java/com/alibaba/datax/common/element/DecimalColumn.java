package com.alibaba.datax.common.element;

import com.alibaba.datax.common.exception.CommonErrorCode;
import com.alibaba.datax.common.exception.DataXException;
import org.apache.commons.lang3.math.NumberUtils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

/**
 * @author ChengJie
 * @desciption
 * @date 2019/5/20 15:34
 **/
public class DecimalColumn extends Column {


    public DecimalColumn(final String data) {
        super(new BigDecimal(0), Type.DECIMAL, 0);
        if (null == data) {
            return;
        }
        try {
            BigDecimal rawData = NumberUtils.createBigDecimal(data);
            super.setRawData(rawData);
            super.setByteSize(data.length());
        } catch (Exception e) {
            throw DataXException.asDataXException(
                    CommonErrorCode.CONVERT_NOT_SUPPORT,
                    String.format("String[%s]不能转为Decimal .", data));
        }
    }

    @Override
    public Long asLong() {
        if (null == this.getRawData()) {
            return null;
        }
        BigDecimal decimal=(BigDecimal)this.getRawData();
        return decimal.longValue();
    }

    @Override
    public Double asDouble() {
        if (null == this.getRawData()) {
            return null;
        }
        BigDecimal decimal=(BigDecimal)this.getRawData();
        return decimal.doubleValue();
    }

    @Override
    public String asString() {
        if (null == this.getRawData()) {
            return null;
        }
        BigDecimal decimal=(BigDecimal)this.getRawData();
        return decimal.toString();
    }

    @Override
    public Date asDate() {
        throw DataXException.asDataXException(
                CommonErrorCode.CONVERT_NOT_SUPPORT, "Long类型不能转为Bytes .");
    }

    @Override
    public byte[] asBytes() {
        throw DataXException.asDataXException(
                CommonErrorCode.CONVERT_NOT_SUPPORT, "Long类型不能转为Bytes .");
    }

    @Override
    public Boolean asBoolean() {
        throw DataXException.asDataXException(
                CommonErrorCode.CONVERT_NOT_SUPPORT, "Long类型不能转为Bytes .");
    }

    @Override
    public BigDecimal asBigDecimal() {
        if (null == this.getRawData()) {
            return null;
        }
        BigDecimal decimal=(BigDecimal)this.getRawData();
        return decimal;
    }

    @Override
    public BigInteger asBigInteger() {
        if (null == this.getRawData()) {
            return null;
        }
        BigDecimal decimal=(BigDecimal)this.getRawData();
        return decimal.toBigInteger();
    }
}
