package com.kzonix.community.blog.controller

import com.kzonix.community.blog.model.services.PublicationManagementService
import io.micronaut.http.MediaType
import io.micronaut.http.annotation.*
import io.reactivex.Flowable
import org.reactivestreams.Publisher

@Controller(value = "/publications", produces = MediaType.TEXT_PLAIN)
class PublicationManagementController {

    PublicationManagementService publicationManagementService;

    PublicationManagementController(PublicationManagementService publicationManagementService) {
        this.publicationManagementService = publicationManagementService;
    }

    @Get("/{id}")
    Publisher<String> getPublications(Flowable<String> id) {
        publicationManagementService.get()
    }

    @Post
    String createPublication() { "" }

    @Put
    String updatePublication() { "" }

    @Delete
    String deletePublication() { "" }
}
