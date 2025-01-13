package ru.otus.core.repository;

import java.util.List;
import java.util.Optional;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import ru.otus.crm.model.Client;

public class DataTemplateHibernate<T> implements DataTemplate<T> {

    private final Class<T> clazz;

    public DataTemplateHibernate(Class<T> clazz) {
        this.clazz = clazz;
    }

    @Override
    public Optional<T> findById(Session session, long id) {
        T entity = session.find(clazz, id);
        if (entity instanceof Client client) {
            Hibernate.initialize(client.getPhones()); // Инициализировать коллекцию phones
        }
        return Optional.ofNullable(entity);
    }

    @Override
    public List<T> findByEntityField(Session session, String entityFieldName, Object entityFieldValue) {
        var criteriaBuilder = session.getCriteriaBuilder();
        var criteriaQuery = criteriaBuilder.createQuery(clazz);
        var root = criteriaQuery.from(clazz);
        criteriaQuery.select(root).where(criteriaBuilder.equal(root.get(entityFieldName), entityFieldValue));

        var query = session.createQuery(criteriaQuery);
        return query.getResultList();
    }

    @Override
    public List<T> findAll(Session session) {
        return session.createQuery(String.format("from %s", clazz.getSimpleName()), clazz)
                .getResultList();
    }

    @Override
    public T insert(Session session, T object) {

        session.saveOrUpdate(object);

        return object;
    }

    @Override
    public T update(Session session, T object) {
        return session.merge(object);
    }
}
