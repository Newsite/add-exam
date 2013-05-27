package add.exam.report.services;

import add.exam.report.repositories.ReportRepository;
import org.springframework.stereotype.Service;

import javax.inject.Inject;

@Service
public class ReportService
{
    @Inject
    private ReportRepository reportRepository;
}
