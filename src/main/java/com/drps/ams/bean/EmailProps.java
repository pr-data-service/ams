package com.drps.ams.bean;

import lombok.Data;

@Data
public class EmailProps {
	String to;
	String subject;
	String text;
	String pathToAttachment;
}
