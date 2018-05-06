package com.belonk.converter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.converter.Converter;
import org.springframework.util.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Administrator on 2016/8/27.
 *
 * @author sunfuchang03@126.com
 */
public class String2DateConverter implements Converter<String, Date> {
    private static final Logger LOG = LoggerFactory.getLogger(String2DateConverter.class);

    private static final SimpleDateFormat SDF_S = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static final SimpleDateFormat SDF_M = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    private static final SimpleDateFormat SDF_H = new SimpleDateFormat("yyyy-MM-dd HH");
    private static final SimpleDateFormat SDF_D = new SimpleDateFormat("yyyy-MM-dd");
    private static final SimpleDateFormat SDF_S_1 = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    private static final SimpleDateFormat SDF_M_1 = new SimpleDateFormat("yyyy/MM/dd HH:mm");
    private static final SimpleDateFormat SDF_H_1 = new SimpleDateFormat("yyyy/MM/dd HH");
    private static final SimpleDateFormat SDF_D_1 = new SimpleDateFormat("yyyy/MM/dd");

    @Override
    public Date convert(String s) {
        if (!StringUtils.hasLength(s))
            return null;
        if (s.indexOf("-") > 0) {
            if (s.indexOf(":") > 0) {
                try {
                    return SDF_S.parse(s);
                } catch (ParseException e) {
                    try {
                        return SDF_M.parse(s);
                    } catch (ParseException e1) {
                        LOG.error("Parse date exception : ", e);
                    }
                }
            } else {
                try {
                    return SDF_H.parse(s);
                } catch (ParseException e) {
                    try {
                        return SDF_D.parse(s);
                    } catch (ParseException e1) {
                        LOG.error("Parse date exception : ", e);
                    }
                }
            }
        } else if (s.indexOf("/") > 0) {
            if (s.indexOf(":") > 0) {
                try {
                    return SDF_S_1.parse(s);
                } catch (ParseException e) {
                    try {
                        return SDF_M_1.parse(s);
                    } catch (ParseException e1) {
                        LOG.error("Parse date exception : ", e);
                    }
                }
            } else {
                try {
                    return SDF_H_1.parse(s);
                } catch (ParseException e) {
                    try {
                        return SDF_D_1.parse(s);
                    } catch (ParseException e1) {
                        LOG.error("Parse date exception : ", e);
                    }
                }
            }
        }
        return null;
    }
}
