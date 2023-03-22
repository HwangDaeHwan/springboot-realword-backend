package com.realworld.backend.domain.article.model;

import javax.validation.constraints.AssertTrue;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FeedParams {

	protected Integer offset;
	protected Integer limit;
	
	@AssertTrue
	protected boolean getValidPage() {
		return (offset != null && limit != null) || (offset == null && limit == null); 
	}
}
