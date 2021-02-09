package com.ilyabuglakov.raise.model.tag;

import lombok.Setter;
import lombok.extern.log4j.Log4j2;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 * The type Timestamp tag. Formats timestamp.
 */
@Setter
@Log4j2
public class TimestampTag extends SimpleTagSupport {

    private String locale;
    private LocalDateTime timestamp;

    @Override
    public void doTag() throws JspException, IOException {
        JspWriter out = getJspContext().getOut();
//        log.info(locale);
        String formattedDate = timestamp.format(DateTimeFormatter.ofPattern("HH:mm:ss dd MMM yyyy", Locale.forLanguageTag(locale)));
        out.print(formattedDate);
    }
}
