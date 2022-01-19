#ifndef DISCODE_DESKTOP_SESSION_SERVICE_H
#define DISCODE_DESKTOP_SESSION_SERVICE_H

#include "session.h"

#include <optional>
#include <string>

class session_service {
public:
    session_service() = default;
    void create_session(std::string jwt);
    void drop_session();
    std::optional<session> get_session();

private:
    std::optional<session> current_session{};
};


#endif //DISCODE_DESKTOP_SESSION_SERVICE_H
