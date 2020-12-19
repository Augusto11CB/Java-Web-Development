package aug.bueno.cloudstorage.controller.exception;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import static aug.bueno.cloudstorage.controller.util.MessageWrapperUtil.MAX_FILE_SIZE_ALLOWED_MSG;

@EnableWebMvc
@ControllerAdvice(basePackages = {"aug.bueno.cloudstorage"})
public class GlobalExceptionHandler {

    @ExceptionHandler({MultipartException.class})
    public String maxUploadSizeExceededExceptionHandler(MultipartException e, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("message", e.getCause().getMessage());
        return "redirect:/result?errorMessage=" + MAX_FILE_SIZE_ALLOWED_MSG;
    }

/*
    @ExceptionHandler({FileNotFoundException.class})
    @ResponseStatus(value= HttpStatus.NOT_FOUND)
    public String fileNotFoundExceptionHandler(FileNotFoundException e, RedirectAttributes redirectAttributes) {
        return "redirect:/result?errorMessage=" + FILE_NOT_FOUND_MSG;
    }

    @ExceptionHandler(FileNotFoundException.class)
    @ResponseStatus(value = HttpStatus.OK)
    public ModelAndView handleCustomRuntimeException(HttpServletRequest request, HttpServletResponse response, Exception ex) {
        ModelAndView mav = new ModelAndView("result");
        mav.addObject("errorMessage", FILE_NOT_FOUND_MSG);
        return mav;
    }

    @ExceptionHandler(FileNotFoundException.class)
    public String handleException(final FileNotFoundException e) {
        return "forward:/n/error";
    }
*/
}