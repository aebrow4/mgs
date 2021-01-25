package graph.dataAccess;

import java.util.Collection;
import java.util.Iterator;
import java.util.Optional;
import java.util.Set;

import org.neo4j.ogm.cypher.ComparisonOperator;
import org.neo4j.ogm.cypher.Filter;
import org.neo4j.ogm.cypher.query.Pagination;
import org.neo4j.ogm.session.Session;


abstract class Neo4jApi<T> {

  private static final int DEPTH_LIST = 0;
  private static final int DEPTH_ENTITY = 1;
  protected GetSession getSession;


  Object[] findAll(Pagination pagination) {
    return getSession.get().loadAll(getEntityType(), pagination, DEPTH_LIST).toArray();
  }

  Iterable<T> findAll(Filter filter) {
    return getSession.get().loadAll(getEntityType(), filter, DEPTH_LIST);
  }

  Iterable<T> findAll() {
    return getSession.get().loadAll(getEntityType(), DEPTH_LIST);
  }

  /**
   * Lookup a record by its discogsId and return the first match.
   * Returns an Optional
   * @param discogsId
   * @return
   */
  Optional<T> getByDiscogsId(Long discogsId) {
    //Filter filter = new Filter("discogsId", ComparisonOperator.EQUALS, discogsId);
    T res = find(discogsId);

    if (res != null) {
      return Optional.of(res);
    } else {
      return Optional.empty();
    }
  }

  /**
   * Lookup all records for the passed in ids. Returns an Iterator
   * @param discogsIds
   * @return
   */
  Iterator<T> getByDiscogsId(Set<Long> discogsIds) {
    Filter filter = new Filter("discogsId", ComparisonOperator.IN, discogsIds);
    Iterator<T> res = findAll(filter).iterator();

    return res;
  }

  public T find(Long id) {
    return getSession.get().load(getEntityType(), id, DEPTH_ENTITY);
  }

  public void create(T entity) {
    getSession.get().save(entity);
  }

  public void create(Iterable<T> entities) {
    getSession.get().save(entities);
  }

  public void update(T entity) {
    getSession.get().save(entity);
  }

  public void update(Iterable<T> entities) {
    getSession.get().save(entities);
  }

  public void delete(Long id) {
    getSession.get().delete(find(id));
  }

  public void deleteAllInPages() {
    Pagination pagination = new Pagination(0, 10);
    // we have to do the find and delete in the same session?
    Session session = getSession.get();
    Collection<T> records = session.loadAll(getEntityType(), pagination, DEPTH_LIST);
    session.delete(records);

    //Object thing = session.load(getEntityType(), 7032992L);
    //Object thing2 = session.load(getEntityType(), 7032990L);
    //Object array[] = { thing, thing2 };
    // still deletes one by one..
    //session.delete(array);
  }

  abstract Class<T> getEntityType();
}
