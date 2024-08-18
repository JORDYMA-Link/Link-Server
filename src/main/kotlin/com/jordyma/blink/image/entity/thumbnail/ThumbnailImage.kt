package com.jordyma.blink.image.entity.thumbnail

import com.jordyma.blink.feed.entity.Feed
import com.jordyma.blink.image.entity.Image
import jakarta.persistence.*

@Entity
@DiscriminatorValue("thumbnail")
class ThumbnailImage(

    @OneToOne @PrimaryKeyJoinColumn(name = "feed_id")
    var feed: Feed,

) : Image(url = "") {

    init {
        feed.thumbnailImage = this
    }

    constructor(feed: Feed, url: String) : this(feed) {
        this.url = url
    }
}
