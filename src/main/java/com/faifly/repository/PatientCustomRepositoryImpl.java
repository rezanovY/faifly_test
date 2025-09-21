package com.faifly.repository;

import com.faifly.model.PatientVisitProjection;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
@RequiredArgsConstructor
public class PatientCustomRepositoryImpl implements PatientCustomRepository {

    private final EntityManager em;

    @Override
    public Page<PatientVisitProjection> findPatientsWithVisitsAndDoctors(
            String search, List<Integer> doctorIds, Pageable pageable) {

        String jpql = """
        SELECT new com.faifly.model.PatientVisitProjection(
            p.id, p.firstName, p.lastName,
            d.id, d.firstName, d.lastName, d.timezone,
            v.startDateTime, v.endDateTime,
            (SELECT COUNT(DISTINCT p2.id)
                FROM Patient p2
                JOIN Visit v2 ON v2.patientId = p2.id
                WHERE v2.doctorId = d.id)
        )
        FROM Patient p
        LEFT JOIN Visit v ON v.patientId = p.id
        LEFT JOIN Doctor d ON v.doctorId = d.id
        WHERE (:search IS NULL OR LOWER(p.firstName) LIKE LOWER(CONCAT('%', :search, '%'))
               OR LOWER(p.lastName) LIKE LOWER(CONCAT('%', :search, '%')))
          AND (:doctorIds IS NULL OR d.id IN :doctorIds)
          AND v.startDateTime = (
              SELECT MAX(v2.startDateTime)
              FROM Visit v2
              WHERE v2.patientId = p.id AND v2.doctorId = d.id
          )
        ORDER BY p.id
        """;

        TypedQuery<PatientVisitProjection> query = em.createQuery(jpql, PatientVisitProjection.class)
                .setParameter("search", search)
                .setParameter("doctorIds", doctorIds == null || doctorIds.isEmpty() ? null : doctorIds)
                .setFirstResult((int) pageable.getOffset())
                .setMaxResults(pageable.getPageSize());

        List<PatientVisitProjection> content = query.getResultList();

        String countJpql = """
        SELECT COUNT(DISTINCT p.id)
        FROM Patient p
        LEFT JOIN Visit v ON v.patientId = p.id
        LEFT JOIN Doctor d ON v.doctorId = d.id
        WHERE (:search IS NULL OR LOWER(p.firstName) LIKE LOWER(CONCAT('%', :search, '%'))
               OR LOWER(p.lastName) LIKE LOWER(CONCAT('%', :search, '%')))
          AND (:doctorIds IS NULL OR d.id IN :doctorIds)
        """;

        TypedQuery<Long> countQuery = em.createQuery(countJpql, Long.class)
                .setParameter("search", search)
                .setParameter("doctorIds", doctorIds == null || doctorIds.isEmpty() ? null : doctorIds);

        long total = countQuery.getSingleResult();

        return new PageImpl<>(content, pageable, total);
    }
}
