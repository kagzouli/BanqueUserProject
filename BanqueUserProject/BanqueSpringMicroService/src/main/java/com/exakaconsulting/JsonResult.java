package com.exakaconsulting;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class JsonResult<T> implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2104368669801397895L;

	private T result;
	
	private List<String> infos = new ArrayList<>();
	
	private List<String> warning =  new ArrayList<>();
	
	private List<String> errors = new ArrayList<>();

	private boolean success;
	
	public T getResult() {
		return result;
	}

	public void setResult(T result) {
		this.result = result;
	}

	public List<String> getInfos() {
		return infos;
	}

	public void setInfos(List<String> infos) {
		this.infos = infos;
	}

	public List<String> getWarning() {
		return warning;
	}

	public void setWarning(List<String> warning) {
		this.warning = warning;
	}

	public List<String> getErrors() {
		return errors;
	}

	public void setErrors(List<String> errors) {
		this.errors = errors;
	}
	
	public void addInfo(final String info){
		this.infos.add(info);
	}
	
	public void addWarning(final String warning){
		this.warning.add(warning);
	}
	
	public void addError(final String error){
		this.success = false;
		this.errors.add(error);
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}
	
}
