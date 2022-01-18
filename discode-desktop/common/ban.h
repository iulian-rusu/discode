#ifndef DISCODE_DESKTOP_BAN_H
#define DISCODE_DESKTOP_BAN_H

#include <string>

struct ban {
    long long banId{};
    std::string user{};
    std::string start{};
    std::string end{};
    std::string reason{};
};

#endif //DISCODE_DESKTOP_BAN_H
