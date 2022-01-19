#ifndef DISCODE_DESKTOP_BAN_H
#define DISCODE_DESKTOP_BAN_H

#include <string>

struct ban {
    ban() = default;
    ban(long long i_banId, std::string i_user, std::string i_start, std::string i_end, std::string i_reason)
        : banId(std::move(i_banId)), user(std::move(i_user)), start(std::move(i_start)), end(std::move(i_end)), reason(std::move(i_reason)) {}

    long long banId{};
    std::string user{};
    std::string start{};
    std::string end{};
    std::string reason{};
};

#endif //DISCODE_DESKTOP_BAN_H
