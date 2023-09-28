package ge.ted3x.revolt.core.data.mapper

abstract class RevoltMapper<ApiModel, DomainModel, EntityModel> {

    open fun mapDomainToEntity(domainModel: DomainModel): EntityModel {
        throw NotImplementedError()
    }

    open fun mapEntityToDomain(entityModel: EntityModel): DomainModel {
        throw NotImplementedError()
    }

    open fun mapApiToEntity(apiModel: ApiModel): EntityModel {
        throw NotImplementedError()
    }
}