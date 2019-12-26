package io.onedev.server.search.entity.issue;

import javax.annotation.Nullable;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import io.onedev.server.model.Issue;
import io.onedev.server.model.Project;
import io.onedev.server.search.entity.EntityQuery;
import io.onedev.server.util.ProjectAwareCommit;
import io.onedev.server.util.query.IssueQueryConstants;

public class FixedInCommitCriteria extends IssueCriteria {

	private static final long serialVersionUID = 1L;

	private final ProjectAwareCommit commit;
	
	private final String value;
	
	public FixedInCommitCriteria(@Nullable Project project, String value) {
		commit = EntityQuery.getCommitId(project, value);
		this.value = value;
	}

	public FixedInCommitCriteria(ProjectAwareCommit commit) {
		this.commit = commit;
		value = commit.getCommitId().name();
	}
	
	@Override
	public Predicate getPredicate(Root<Issue> root, CriteriaBuilder builder) {
		if (!commit.getFixedIssueNumbers().isEmpty()) {
			return builder.and(
					builder.equal(root.get(IssueQueryConstants.ATTR_PROJECT), commit.getProject()),
					root.get(IssueQueryConstants.ATTR_NUMBER).in(commit.getFixedIssueNumbers()));
		} else {
			return builder.disjunction();
		}
	}

	@Override
	public boolean matches(Issue issue) {
		return issue.getProject().equals(commit.getProject()) 
				&& commit.getFixedIssueNumbers().contains(issue.getNumber());
	}

	@Override
	public String asString() {
		return IssueQuery.getRuleName(IssueQueryLexer.FixedInCommit) + " " + quote(value);
	}

}
