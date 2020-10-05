package com.dark.mode.demo.resource.v1;

import com.dark.mode.demo.exception.ResourceNotAcceptableException;
import com.dark.mode.demo.exception.ResourceNotFoundException;
import com.dark.mode.demo.model.Student;
import com.dark.mode.demo.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/student")
public class StudentResource {
    private final StudentService studentService;

    @GetMapping("/all")
    public Iterable<Student> all() {
        return studentService.getAllStudents();
    }

    @GetMapping("/{id}")
    public Student find(@PathVariable Integer id) {
        return studentService.getStudentById(id).orElseThrow(() -> new ResourceNotFoundException("Student", "id", id));
    }

    @PostMapping("/save")
    public Student save(@RequestBody Student s) {
        if (s.getId() != null) {
            throw new ResourceNotAcceptableException("id", s.getId());
        }
        s.getStudentSubjects().forEach(studentSubject -> studentSubject.setStudent(s));
        return studentService.saveStudent(s);
    }

    @PatchMapping("/update")
    public Student update(@RequestBody Student s) {
        if (s.getId() == null) {
            throw new ResourceNotFoundException("Student", "id", null);
        }
        return studentService.updateStudent(s);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable Integer id) {
        var s = find(id);
        studentService.deleteStudent(s);
        return new ResponseEntity<>("student deleted", HttpStatus.OK);
    }
}
