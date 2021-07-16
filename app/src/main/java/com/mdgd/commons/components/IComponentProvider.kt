package com.mdgd.commons.components

import com.mdgd.commons.components.repo.IRepo

interface IComponentProvider {
    fun getRepo(): IRepo

}
