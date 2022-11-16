package com.ghsoares.chirper.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class OffsetLimitPageable implements Pageable {
	private Sort sort;
	private int offset;
	private int limit;
	private boolean paged;
	
	protected OffsetLimitPageable(int offset, int limit, boolean paged, Sort sort) {
		this.offset = offset;
		this.limit = limit;
		this.paged = paged;
		this.sort = sort;
	}
	
	@Override
	public int getPageNumber() {
		return 0;
	}
	
	@Override
	public int getPageSize() {
		return paged ? limit : Integer.MAX_VALUE;
	}
	
	@Override
	public long getOffset() {
		return this.offset;
	}
	
	@Override
	public Sort getSort() {
		return sort;
	}
	
	@Override
	public Pageable previousOrFirst() {
		return this;
	}
	
	@Override
	public Pageable next() {
		return this;
	}
	
	@Override
	public boolean hasPrevious() {
		return false;
	}
	
	@Override
	public boolean isPaged() {
        return true;
    }
	
	@Override
	public Pageable first() {
		return this;
	}

	@Override
	public Pageable withPage(int pageNumber) {
		return this;
	}
	
	public static OffsetLimitPageable of(int offset, int limit, Sort sort) {
		return new OffsetLimitPageable(offset, limit, true, sort);
	}
	
	public static OffsetLimitPageable of(int offset, int limit) {
		return of(offset, limit, Sort.unsorted());
	}
	
	public static OffsetLimitPageable unpaged(Sort sort) {
		return new OffsetLimitPageable(0, 1, false, sort);
	}

}
