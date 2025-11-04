package com.uravgcode.modernessentials.update;

import com.google.gson.JsonParser;
import org.apache.maven.artifact.versioning.ComparableVersion;
import org.jetbrains.annotations.NotNull;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.concurrent.CompletableFuture;

public final class UpdateChecker {
    private final HttpClient httpClient;
    private final HttpRequest httpRequest;

    public UpdateChecker() {
        final var url = URI.create("https://api.github.com/repos/UrAvgCode/modern-essentials/releases/latest");
        final var timeout = Duration.ofSeconds(5);

        this.httpClient = HttpClient.newBuilder()
            .connectTimeout(timeout)
            .build();

        this.httpRequest = HttpRequest.newBuilder()
            .uri(url)
            .timeout(timeout)
            .header("Accept", "application/vnd.github+json")
            .GET()
            .build();
    }

    public @NotNull CompletableFuture<ComparableVersion> fetchLatestVersion() {
        return httpClient.sendAsync(httpRequest, HttpResponse.BodyHandlers.ofString())
            .thenApply(response -> parseResponse(response.body()))
            .exceptionally(throwable -> new ComparableVersion("0.0.0"));
    }

    private @NotNull ComparableVersion parseResponse(@NotNull String jsonString) {
        try {
            final var json = JsonParser.parseString(jsonString).getAsJsonObject();
            final var tagName = json.get("tag_name").getAsString();
            return new ComparableVersion(tagName);
        } catch (Exception exception) {
            return new ComparableVersion("0.0.0");
        }
    }
}
