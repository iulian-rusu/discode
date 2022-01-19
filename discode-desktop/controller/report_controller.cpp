#include "report_controller.h"

report_controller::report_controller(std::shared_ptr<report_space::report_service> i_rs, std::shared_ptr<session_service> i_ss, std::shared_ptr<report_model> i_rm, QObject *parent)
    : QObject(parent), rs(i_rs), ss(i_ss), rm(i_rm) { }

void report_controller::onRefresh() {
    auto const &session = ss->get_session();

    auto on_get = [this](std::vector<report> reports) {
        rm->replaceAll(reports);
    };
    auto on_failure = [this]() {
        emit errorOccured();
    };
    rs->get(session.value(), on_get, on_failure);
}

void report_controller::onReview(long long message_id) {
    auto const &session = ss->get_session();

    auto on_success = [this]() {
        onRefresh();
        emit messageReviewed();
    };
    auto on_failure = [this]() {
        emit errorOccured();
    };
    rs->review(message_id, session.value(), on_success, on_failure);
}
