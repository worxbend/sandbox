package io.kzonix.redprime.tasks

import play.api.inject.SimpleModule
import play.api.inject.bind

class TasksModule
    extends SimpleModule(
      bind[RedditUserOverviewTask].toSelf.eagerly()
    )
