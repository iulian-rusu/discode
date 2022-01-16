#include "report_controller.h"

#include <QDebug>

void report_controller::onReportReviewed(long long messageId) {
    qDebug() << "Report reviewed " << messageId;
}
