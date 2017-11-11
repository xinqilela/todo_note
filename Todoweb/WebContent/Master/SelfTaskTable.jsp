<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<table id="showtasks">
	<thead>
		<tr>
			<th>标题</th>
			<th>描述</th>
			<th>开始时间</th>
			<th>结束时间</th>
			<th>提醒时间</th>
			<th>项目</th>
			<th>目标</th>
			<th>情景</th>
			<th colspan="3">操作</th>
		</tr>
	</thead>

	<s:iterator value="selftasks">
		<tr>
			<td width="10%" class="other"><s:property value="title" /></td>
			<td width="10%" class="other"><s:property value="content" /></td>
			<td width="10%" class="other"><s:property value="starttime" /></td>
			<td width="10%" class="other"><s:property value="endtime" /></td>
			<td width="10%" class="other"><s:property value="clocktime" /></td>
			<td width="10%" class="other"><s:property value="projectId" /></td>
			<td width="10%" class="other"><s:property value="goalId" /></td>
			<td width="10%" class="other"><s:property value="sightId" /></td>
			<td width="5%" class="btnTask">编辑</td>
			<td width="5%" class="btnTask">完成</td>
			<td width="5%" class="btnTask">删除</td>
		</tr>
	</s:iterator>
</table>