#ifndef DISCODE_DESKTOP_REPORT_CONTROLLER_H
#define DISCODE_DESKTOP_REPORT_CONTROLLER_H

#include "report_model.h"
#include "report_service.h"
#include "session_service.h"

#include <memory>

#include <QObject>
#include <QString>

class report_controller : public QObject {
    Q_OBJECT
public:
    explicit report_controller(std::shared_ptr<report_space::report_service>, std::shared_ptr<session_service>, std::shared_ptr<report_model>, QObject * = nullptr);

signals:
    void errorOccured();
    void userReported();
    void messageReviewed();

public slots:
    void onRefresh();
    void onReview(long long);

private:
    std::shared_ptr<report_space::report_service> rs{};
    std::shared_ptr<session_service> ss{};
    std::shared_ptr<report_model> rm{};
};


#endif //DISCODE_DESKTOP_REPORT_CONTROLLER_H
