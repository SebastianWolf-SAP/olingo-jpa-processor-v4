package org.apache.olingo.jpa.processor.core.filter;

import javax.persistence.criteria.Path;
import javax.persistence.criteria.Root;

import org.apache.olingo.jpa.metadata.core.edm.mapper.api.JPADescriptionAttribute;
import org.apache.olingo.jpa.metadata.core.edm.mapper.api.JPAElement;
import org.apache.olingo.jpa.metadata.core.edm.mapper.api.JPAEntityType;
import org.apache.olingo.jpa.metadata.core.edm.mapper.api.JPAPath;
import org.apache.olingo.jpa.metadata.core.edm.mapper.exception.ODataJPAModelException;
import org.apache.olingo.jpa.processor.core.query.JPAAbstractQuery;
import org.apache.olingo.jpa.processor.core.query.Util;
import org.apache.olingo.server.api.uri.UriInfoResource;

public class JPAMemberOperator implements JPAOperator {
  private final UriInfoResource member;
  private final JPAEntityType jpaEntityType;
  private final Root<?> root;

  JPAMemberOperator(final JPAEntityType jpaEntityType, final JPAAbstractQuery parent,
      final UriInfoResource member) {
    super();
    this.member = member;
    this.jpaEntityType = jpaEntityType;
    this.root = parent.getRoot();
  }

  @Override
  public Path<?> get() {
    final JPAPath selectItemPath = determineAttributePath();
    return determineCriteriaPath(selectItemPath);
  }

  private Path<?> determineCriteriaPath(final JPAPath selectItemPath) {
    Path<?> p = root;
    for (final JPAElement jpaPathElement : selectItemPath.getPath()) {
      if (jpaPathElement instanceof JPADescriptionAttribute) {
        // TODO handle description fields
//        Join<?, ?> join = (Join<?, ?>) joinTables.get(jpaPathElement.getInternalName());
//        p = join.get(((JPADescriptionAttribute) jpaPathElement).getDescriptionAttribute().getInternalName());
      } else
        p = p.get(jpaPathElement.getInternalName());
    }
    return p;
  }

  public JPAPath determineAttributePath() {
    final String path = Util.determineProptertyNavigationPath(member.getUriResourceParts());
    JPAPath selectItemPath = null;
    try {
      selectItemPath = jpaEntityType.getPath(path);
    } catch (ODataJPAModelException e) {
      // TODO error handling
      e.printStackTrace();
    }
    return selectItemPath;
  }

  public UriInfoResource getMember() {
    return member;
  }
}
