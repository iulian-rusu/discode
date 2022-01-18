#ifndef DISCODE_DESKTOP_REPORT_H
#define DISCODE_DESKTOP_REPORT_H

#include <string>

struct report {
    long long messageId{};
    long long userId{};
    std::string message{};
    std::string issuer{};
};

#endif //DISCODE_DESKTOP_REPORT_H
