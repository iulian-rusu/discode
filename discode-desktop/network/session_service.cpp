#include "session_service.h"

void session_service::create_session(std::string jwt) {
    current_session.emplace(std::move(jwt));
}

void session_service::drop_session() {
    current_session.reset();
}

std::optional<session> session_service::get_session() {
    return current_session;
}
