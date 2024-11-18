package br.com.smms.exceptions;

import java.io.Serializable;
import java.time.LocalDateTime;

public class ExceptionResponse implements Serializable {

	private static final long serialVersionUID = 3637455825837838560L;

	private LocalDateTime localDateTime;
	private String message;
	private String details;
	
	public ExceptionResponse(LocalDateTime localDateTime, String message, String details) {
		this.localDateTime = localDateTime;
		this.message = message;
		this.details = details;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public LocalDateTime getLocalDateTime() {
		return localDateTime;
	}

	public String getMessage() {
		return message;
	}

	public String getDetails() {
		return details;
	}

}
