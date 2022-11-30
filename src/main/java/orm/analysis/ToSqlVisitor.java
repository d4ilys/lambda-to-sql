package orm.analysis;

import co.streamx.fluent.extree.expression.*;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

import static co.streamx.fluent.extree.expression.ExpressionType.LogicalOr;


public class ToSqlVisitor implements ExpressionVisitor<StringBuilder> {

    private Stack<String> _conditionStack = new Stack<String>();
    private StringBuilder sb = new StringBuilder();
    private Expression body;
    private Queue<ConstantExpression> normalParam = new LinkedList<>();
    private Queue<ConstantExpression> arrayListParam = new LinkedList<>();
    private Queue<InvocationExpression> invocationExpressionQueue = new LinkedList<>();

    public String GetWhere() {
        for (String s : _conditionStack) {
            sb.append(s);
        }
        return sb.toString();
    }

    @Override
    public StringBuilder visit(BinaryExpression e) {
        boolean quote = e != body && e.getExpressionType() == LogicalOr;
        e.getFirst().accept(this);
        _conditionStack.add(" ");
        String handlerType = SqlOperator.relatorHandler(e.getExpressionType());
        _conditionStack.add(handlerType);
        _conditionStack.add(" ");
        e.getSecond().accept(this);
        if (quote) _conditionStack.add(")");

        return sb;
    }

    @Override
    public StringBuilder visit(ConstantExpression e) {
        //处理数组
        if (e.getValue() instanceof ArrayList) {
            ArrayList array = (ArrayList) e.getValue();
            String r = SqlOperator.InHandler(array);
            _conditionStack.add(r);
        } else {
            _conditionStack.add("'" + e.getValue().toString() + "'");
        }
        return sb;
    }

    @Override
    public StringBuilder visit(InvocationExpression e) {
        //参数是Constant
        e.getArguments().stream().filter(x -> x instanceof ConstantExpression).forEach(x -> {
            ConstantExpression constantExpression = (ConstantExpression) x;
            if (constantExpression.getValue() instanceof ArrayList) {
                arrayListParam.add(constantExpression);
            } else {
                normalParam.add(constantExpression);
            }
        });
        //参数是字段 p.getId()
        e.getArguments().stream().filter(x -> x instanceof InvocationExpression).forEach(x -> {
            invocationExpressionQueue.add((InvocationExpression) x);
        });
        return e.getTarget().accept(this);
    }

    @Override
    public StringBuilder visit(MemberExpression e) {
        String name = e.getMember().getName();
        var resultType = "";
        if (e.getInstance() != null) {
            resultType = e.getInstance().getResultType().toString();
        }
        var type = SqlOperator.methodIsHandler(name, resultType);
        if (type == 1) {
            String format = "";
            switch (name) {
                case "contains":
                    format = "{0} LIKE ''%{1}%'' ";
                    break;
                case "startsWith":
                    format = "{0} LIKE ''%{1}'' ";
                    break;
                case "endsWith":
                    format = "{0} LIKE ''{1}%'' ";
                    break;
            }
            //处理前面的属性名称
            //如果直接是字段名
            if (e.getInstance() instanceof MemberExpression) {
                visit((MemberExpression) e.getInstance());
            } else if (e.getInstance() instanceof InvocationExpression) {
                //如果是方法 直接处理。
                InvocationExpression target = (InvocationExpression) e.getInstance();
                target.getTarget().accept(this);
            }
            //处理参数
            for (ParameterExpression parameter : e.getParameters()) {
                visit(parameter);
            }
            //获取参数和属性
            String parameter = _conditionStack.pop();
            String property = _conditionStack.pop();
            String result = MessageFormat.format(format, property, parameter.replace("'",""));
            _conditionStack.add(result);
        } else if (type == 2) {
            invocationExpressionQueue.poll().getTarget().accept(this);
            String formats = "";
            switch (name) {
                case "contains":
                    formats = "{0} IN ({1})";
                    break;
            }
            //处理参数
            //TODO:这里就支持一个Contains
            arrayListParam.poll().accept(this);
            //获取参数和属性
            String value = _conditionStack.pop();
            String parameter = _conditionStack.pop();
            String result = MessageFormat.format(formats, parameter, value);
            _conditionStack.add(result);
        } else {
            String p = e.getMember().getName();
            p = p.replaceAll("^(get)", "");
            p = p.substring(0, 1).toLowerCase() + p.substring(1);
            _conditionStack.add(p);
        }
        return sb;
    }

    @Override
    public StringBuilder visit(LambdaExpression<?> e) {
        this.body = e.getBody();
        return body.accept(this);
    }

    @Override
    public StringBuilder visit(DelegateExpression delegateExpression) {
        String str = delegateExpression.getDelegate().getResultType().getName();
        return null;
    }

    @Override
    public StringBuilder visit(ParameterExpression e) {
        while (normalParam.stream().count() > 0)
            normalParam.poll().accept(this);
        return sb;
    }

    @Override
    public StringBuilder visit(UnaryExpression e) {
        sb.append(SqlOperator.relatorHandler(e.getExpressionType()));
        return e.getFirst().accept(this);
    }

    @Override
    public StringBuilder visit(BlockExpression blockExpression) {
        return null;
    }

    @Override
    public StringBuilder visit(NewArrayInitExpression newArrayInitExpression) {
        return null;
    }

}