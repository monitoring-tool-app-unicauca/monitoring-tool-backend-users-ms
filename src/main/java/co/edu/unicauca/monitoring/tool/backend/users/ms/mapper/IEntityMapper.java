package co.edu.unicauca.monitoring.tool.backend.users.ms.mapper;

/**
 * An interface representing a generic mapper between DTO (Data Transfer Object) and domain entity.
 *
 * @param <D> Type parameter representing the DTO (Data Transfer Object).
 * @param <E> Type parameter representing the domain entity.
 */
public interface IEntityMapper<D, E> {
    /**
     * Maps a DTO (Data Transfer Object) to a domain entity.
     *
     * @param dto The DTO object to be mapped to the domain entity.
     * @return The domain entity.
     */
    E toDomain(D dto);

    /**
     * Maps a domain entity to a DTO (Data Transfer Object).
     *
     * @param entity The domain entity to be mapped to the DTO.
     * @return The DTO object.
     */
    D toDto(E entity);
}