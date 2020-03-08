package com.netposa.vetl.core;

import com.netposa.vetl.dao.JobRecordMapper;
import com.netposa.vetl.entity.JobRecord;
import com.netposa.vetl.entity.SysConf;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @program: vetl-quartz-server
 * @description: 任务核心逻辑
 * @author: kangyu
 * @create: 2020-01-06 15:04
 **/
@DisallowConcurrentExecution
@Component
@Slf4j
public class JobCore implements Job {


    @Autowired
    JobRecordMapper jobRecordMapper;


    @Override
    public void execute(JobExecutionContext executorContext) throws JobExecutionException {
        JobDataMap map = executorContext.getMergedJobDataMap();
        String jarPath = map.getString("jarPath");
        String parameter = map.getString("parameter");
        String vmParam = map.getString("vmParam");
        log.info("Running Job name : {} ", map.getString("name"));
        log.info("Running Job description : {}", map.getString("jobDescription"));
        log.info("Running Job group: {} ", map.getString("jobGroup"));
        log.info(String.format("Running Job cron : %s", map.getString("cronExpression")));
        log.info("Running Job jar path : {} ", jarPath);
        log.info("Running Job parameter : {} ", parameter);
        log.info("Running Job vmParam : {} ", vmParam);
        long startTime = System.currentTimeMillis();
        String logStr ="";
        JobRecord jobRecord = new JobRecord();
        if (!StringUtils.isEmpty(jarPath)) {
            File jar = new File(jarPath);
            if (jar.exists()) {
                ProcessBuilder processBuilder = new ProcessBuilder();
                processBuilder.directory(jar.getParentFile());
                List<String> commands = new ArrayList<>();
                commands.add("java");
                if (!StringUtils.isEmpty(vmParam)) commands.add(vmParam);
                commands.add("-jar");
                commands.add(jarPath);
                if (!StringUtils.isEmpty(parameter)) commands.add(parameter);
                processBuilder.command(commands);
                log.info("Running Job details as follows >>>>>>>>>>>>>>>>>>>>: ");

                try {
                    Process process = processBuilder.start();

                    Map<String, String> logMap = logProcess(process.getInputStream(), process.getErrorStream());
                    logStr = logMap.get("log");
                    int volume = Integer.parseInt(logMap.get("volume"));
                    if (volume>=0){
                        jobRecord.setDataVolume(volume);
                    }
                } catch (IOException e) {
                    throw new JobExecutionException(e);
                }finally {

                }
            } else throw new JobExecutionException("Job Jar not found >>  " + jarPath);
        }
        long endTime = System.currentTimeMillis();

        jobRecord.setLog(logStr);
        jobRecord.setCostTime((endTime - startTime));
        jobRecord.setJobId(Integer.parseInt(map.getString("id")));
        String dateStr = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        jobRecord.setJobDate(dateStr);
        jobRecordMapper.insert(jobRecord);
        log.info(">>>>>>>>>>>>> Running Job has been completed , cost time : {}ms\n ", (endTime - startTime));
    }

    //记录Job执行内容
    private Map<String,String> logProcess(InputStream inputStream, InputStream errorStream) throws IOException {
        String inputLine;
        String errorLine;
        BufferedReader inputReader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
        BufferedReader errorReader = new BufferedReader(new InputStreamReader(errorStream, StandardCharsets.UTF_8));
        StringBuilder builder = new StringBuilder();

        int volume = -1;

        while (Objects.nonNull(inputLine = inputReader.readLine())){
            if (isError(inputLine)){
                builder.append("<p style=\"color:red;\">").append(inputLine).append("</p>");
            }else if (isVolumeLine(inputLine)){
                volume = getVolume(inputLine);
                continue;
            }
            else {
                builder.append(inputLine).append("<br/>");
            }
            log.info(inputLine);
        }
        while (Objects.nonNull(errorLine = errorReader.readLine())) {
            builder.append("<p style=\"color:red;\">").append(errorLine).append("</p>");
            log.error(errorLine);
        }
        HashMap<String, String> map = new HashMap<>();
        map.put("log",builder.toString());
        map.put("volume",volume+"");
        return map;
    }


    boolean isError(String line) {
        return line.contains("error")||line.contains("Exception")||line.contains("Error")||line.contains("ERROR")||line.contains("EXCEPTION")||line.contains("at ");
    }


    boolean isVolumeLine(String line) {
        String keyLine = SysConf.SYS_CONF.getDataStatistical();
        return line.contains(keyLine);
    }


    int getVolume(String line) throws IOException {
//        BufferedReader inputReader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
//        String line = inputReader.readLine();
//        if (StringUtils.isEmpty(line)){
//            return -1;
//        }
//        if (!isVolumeLine(line)){
//            return -1;
//        }

//        String[] split = ":".split(line);
        String[] split = line.split(":");
        if (split.length<2){
            return -1;
        }else {
            try {
                return Integer.parseInt(split[1]);
            }catch (NumberFormatException e) {
                return -1;
            }
        }
    }


}
