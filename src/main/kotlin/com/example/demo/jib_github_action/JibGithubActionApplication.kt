package com.example.demo.jib_github_action

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class JibGithubActionApplication

fun main(args: Array<String>) {
	runApplication<JibGithubActionApplication>(*args)
}
