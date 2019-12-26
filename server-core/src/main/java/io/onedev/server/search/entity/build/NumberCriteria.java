package io.onedev.server.search.entity.build;

import javax.annotation.Nullable;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import io.onedev.server.model.Build;
import io.onedev.server.model.Project;
import io.onedev.server.search.entity.EntityCriteria;
import io.onedev.server.search.entity.EntityQuery;
import io.onedev.server.util.ProjectScopedNumber;
import io.onedev.server.util.query.BuildQueryConstants;

public class NumberCriteria extends EntityCriteria<Build> {

	private static final long serialVersionUID = 1L;

	private final int operator;
	
	private final String value;
	
	private final ProjectScopedNumber number;
	
	public NumberCriteria(@Nullable Project project, String value, int operator) {
		this.operator = operator;
		this.value = value;
		number = EntityQuery.getEntityNumber(project, value);
	}

	@Override
	public Predicate getPredicate(Root<Build> root, CriteriaBuilder builder) {
		Path<Long> attribute = root.get(BuildQueryConstants.ATTR_NUMBER);
		Predicate numberPredicate;
		if (operator == BuildQueryLexer.Is)
			numberPredicate = builder.equal(attribute, number.getNumber());
		else if (operator == BuildQueryLexer.IsGreaterThan)
			numberPredicate = builder.greaterThan(attribute, number.getNumber());
		else
			numberPredicate = builder.lessThan(attribute, number.getNumber());
		return builder.and(
				builder.equal(root.get(BuildQueryConstants.ATTR_PROJECT), number.getProject()),
				numberPredicate);
	}

	@Override
	public boolean matches(Build build) {
		if (build.getProject().equals(number.getProject())) {
			if (operator == BuildQueryLexer.Is)
				return build.getNumber() == number.getNumber();
			else if (operator == BuildQueryLexer.IsGreaterThan)
				return build.getNumber() > number.getNumber();
			else
				return build.getNumber() < number.getNumber();
		} else {
			return false;
		}
	}

	@Override
	public String asString() {
		return quote(BuildQueryConstants.FIELD_NUMBER) + " " 
				+ BuildQuery.getRuleName(operator) + " " 
				+ quote(value);
	}

}
