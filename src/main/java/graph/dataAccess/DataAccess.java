package graph.dataAccess;

import java.util.Iterator;
import java.util.Optional;

import org.neo4j.ogm.cypher.ComparisonOperator;
import org.neo4j.ogm.cypher.Filter;

abstract class DataAccess<T> {

  private static final int DEPTH_LIST = 0;
  private static final int DEPTH_ENTITY = 1;
  protected GetSession getSession;


  Iterable<T> findAll(Filter filter) {
    return getSession.get().loadAll(getEntityType(), filter, DEPTH_LIST);
  }

  Iterable<T> findAll() {
    return getSession.get().loadAll(getEntityType(), DEPTH_LIST);
  }

  Optional<T> getByDiscogsId(Long discogsId) {
    Filter filter = new Filter("discogsId", ComparisonOperator.EQUALS, discogsId);
    Iterator<T> res = findAll(filter).iterator();

    if (res.hasNext()) {
      return Optional.of(res.next());
    } else {
      return Optional.empty();
    }
  }

  public T find(Long id) {
    return getSession.get().load(getEntityType(), id, DEPTH_ENTITY);
  }

  public void create(T entity) {
    getSession.get().save(entity);
  }

  public void delete(Long id) {
    getSession.get().delete(find(id));
  }

  public void deleteAll() {
    getSession.get().delete(findAll());
  }

  abstract Class<T> getEntityType();
}
