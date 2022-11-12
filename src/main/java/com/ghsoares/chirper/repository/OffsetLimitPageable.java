package com.ghsoares.chirper.repository;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

public class OffsetLimitPageable extends PageRequest {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3997705840454129078L;
	
	private int offset;
	
	protected OffsetLimitPageable(int offset, int limit, Sort sort) {
		super(offset, limit, sort);
		this.offset = offset;
	}
	
	@Override
	public long getOffset() {
		return this.offset;
	}
	
	public static OffsetLimitPageable of(int offset, int limit, Sort sort) {
		return new OffsetLimitPageable(offset, limit, sort);
	}
	
	public static OffsetLimitPageable of(int offset, int limit) {
		return of(offset, limit, Sort.unsorted());
	}
}
