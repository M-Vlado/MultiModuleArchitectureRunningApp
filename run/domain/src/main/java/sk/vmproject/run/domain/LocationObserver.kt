package sk.vmproject.run.domain

import kotlinx.coroutines.flow.Flow
import sk.vmproject.core.domain.location.LocationWithAltitude

interface LocationObserver {

    fun observeLocation(interval: Long): Flow<LocationWithAltitude>
}