package fr.cleanarchitecture.easyteach.course.application.usecases.commands;

import an.awesome.pipelinr.Command;
import fr.cleanarchitecture.easyteach.course.domain.model.Course;
import fr.cleanarchitecture.easyteach.shared.domain.viewmodel.BaseViewModel;

import java.util.List;

public class GetAllCoursesCommand implements Command<BaseViewModel<List<Course>>> {
}
