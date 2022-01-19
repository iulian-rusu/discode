#ifndef DISCODE_DESKTOP_NETWORK_HELPERS_H
#define DISCODE_DESKTOP_NETWORK_HELPERS_H

#include "api_config.h"
#include "service_ips.h"
#include "service_routes.h"

#include <algorithm>
#include <unordered_map>
#include <ranges>
#include <string>
#include <sstream>
#include <utility>

#include <QNetworkRequest>
#include <QString>
#include <QUrl>

namespace network_helpers {
    static QUrl get_url(std::shared_ptr<api_config> ac, service_ips ip, service_routes route, std::unordered_map<std::string, std::string> replacements = {}) {
        auto const ip_string{ac->get(ip)};
        auto const route_string{ac->get(route)};
        auto url_string{QString::fromStdString(ip_string + route_string)};

        std::ranges::for_each(replacements, [&url_string](auto const &pair) {
            url_string.replace(QString::fromStdString(pair.first), QString::fromStdString(pair.second));
        });

        return url_string;
    }

    static QNetworkRequest get_authentication_request(std::shared_ptr<api_config> ac) {
        static QNetworkRequest request;
        if (request.url().isEmpty()) {
            static QUrl const url{get_url(ac, service_ips::Backend, service_routes::Authenticate)};
            request.setUrl(url);
            request.setHeader(QNetworkRequest::ContentTypeHeader, "application/json");
            request.setRawHeader(QByteArray{"Accept"}, "*/*");
            request.setRawHeader(QByteArray{"Cache-Control"}, "no-cache");
        }
        return request;
    }

    static QByteArray get_authentication_payload(std::string username, std::string password) {
        std::ostringstream buffer{};
        buffer << R"({"username": ")" << username << R"(","password": ")" << password << "\"}";
        return QByteArray::fromStdString(buffer.str());
    }

    static QNetworkRequest get_ban_request(std::shared_ptr<api_config> ac, std::string token) {
        static QNetworkRequest request;
        if (request.url().isEmpty()) {
            static QUrl const url{get_url(ac, service_ips::Backend, service_routes::Bans)};
            request.setUrl(url);
            request.setHeader(QNetworkRequest::ContentTypeHeader, "application/json");
            request.setRawHeader(QByteArray{"Accept"}, "*/*");
            request.setRawHeader(QByteArray{"Cache-Control"}, "no-cache");
        }
        request.setRawHeader(QByteArray{"Authorization"}, QByteArray::fromStdString(std::string("Bearer ") + token));
        return request;
    }

    static QByteArray get_ban_payload(long long user_id, long long seconds, std::string reason) {
        std::ostringstream buffer{};
        buffer << R"({"userId": ")" << user_id << R"(","seconds": ")" << seconds << R"(","banReason": ")" << reason << "\"}";
        return QByteArray::fromStdString(buffer.str());
    }

    static QNetworkRequest get_unban_request(std::shared_ptr<api_config> ac, std::string token, long long ban_id) {
        static QNetworkRequest request;
        if (request.url().isEmpty()) {
            request.setHeader(QNetworkRequest::ContentTypeHeader, "application/json");
            request.setRawHeader(QByteArray{"Accept"}, "*/*");
            request.setRawHeader(QByteArray{"Cache-Control"}, "no-cache");
        }
        QUrl const url{get_url(ac, service_ips::Backend, service_routes::DeleteBan, {{ "{id}", std::to_string(ban_id) }})};
        request.setUrl(url);
        request.setRawHeader(QByteArray{"Authorization"}, QByteArray::fromStdString(std::string("Bearer ") + token));
        return request;
    }

    static QNetworkRequest get_put_report_request(std::shared_ptr<api_config> ac, std::string token) {
        static QNetworkRequest request;
        if (request.url().isEmpty()) {
            static QUrl const url{get_url(ac, service_ips::Backend, service_routes::PutReports)};
            request.setUrl(url);
            request.setHeader(QNetworkRequest::ContentTypeHeader, "application/json");
            request.setRawHeader(QByteArray{"Accept"}, "*/*");
            request.setRawHeader(QByteArray{"Cache-Control"}, "no-cache");
        }
        request.setRawHeader(QByteArray{"Authorization"}, QByteArray::fromStdString(std::string("Bearer ") + token));
        return request;
    }

    static QByteArray get_report_payload(long long message_id) {
        std::ostringstream buffer{};
        buffer << R"({"messageId": ")" << message_id << R"(","status": "REVIEWED"})";
        return QByteArray::fromStdString(buffer.str());
    }

    static QNetworkRequest get_report_request(std::shared_ptr<api_config> ac, std::string token) {
        static QNetworkRequest request;
        if (request.url().isEmpty()) {
            static QUrl const url{get_url(ac, service_ips::Backend, service_routes::GetReports)};
            request.setUrl(url);
            request.setHeader(QNetworkRequest::ContentTypeHeader, "application/json");
            request.setRawHeader(QByteArray{"Accept"}, "*/*");
            request.setRawHeader(QByteArray{"Cache-Control"}, "no-cache");
        }
        request.setRawHeader(QByteArray{"Authorization"}, QByteArray::fromStdString(std::string("Bearer ") + token));
        return request;
    }
};


#endif //DISCODE_DESKTOP_NETWORK_HELPERS_H
