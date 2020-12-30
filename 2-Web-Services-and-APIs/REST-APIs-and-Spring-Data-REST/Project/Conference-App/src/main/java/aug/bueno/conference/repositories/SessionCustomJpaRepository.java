package aug.bueno.conference.repositories;

import aug.bueno.conference.models.Session;

import java.util.List;

public interface SessionCustomJpaRepository {
    List<Session> customGetSessions();
}
