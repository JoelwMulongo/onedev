package io.onedev.server.search.entity.pullrequest;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import io.onedev.server.model.PullRequest;

import io.onedev.server.search.entity.EntityCriteria;

public class OpenCriteria extends EntityCriteria<PullRequest> {

	private static final long serialVersionUID = 1L;

	private EntityCriteria<PullRequest> getCriteria() {
		return new StatusCriteria(PullRequest.STATE_OPEN);
	}
	
	@Override
	public Predicate getPredicate(Root<PullRequest> root, CriteriaBuilder builder) {
		return getCriteria().getPredicate(root, builder);
	}

	@Override
	public boolean matches(PullRequest request) {
		return getCriteria().matches(request);
	}

	@Override
	public String asString() {
		return PullRequestQuery.getRuleName(PullRequestQueryLexer.Open);
	}

}
