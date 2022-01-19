#ifndef DISCODE_DESKTOP_SESSION_H
#define DISCODE_DESKTOP_SESSION_H

#include <string>

struct session {
    session() = default;
    session(std::string i_token) : token(std::move(i_token)) {}
    std::string token{};
};

#endif //DISCODE_DESKTOP_SESSION_H
