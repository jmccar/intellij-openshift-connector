package org.jboss.tools.intellij.openshift.actions.application;

import com.intellij.openapi.actionSystem.AnActionEvent;
import org.jboss.tools.intellij.openshift.tree.LazyMutableTreeNode;
import org.jboss.tools.intellij.openshift.tree.application.ApplicationNode;
import org.jboss.tools.intellij.openshift.tree.application.ApplicationTreeModel;
import org.jboss.tools.intellij.openshift.utils.ExecHelper;
import org.jboss.tools.intellij.openshift.utils.UIHelper;

import javax.swing.JOptionPane;
import javax.swing.tree.TreePath;
import java.io.IOException;
import java.util.concurrent.CompletableFuture;

public class DeleteApplicationAction extends OdoAction {
  public DeleteApplicationAction() {
    super(ApplicationNode.class);
  }

  @Override
  public void actionPerformed(AnActionEvent anActionEvent, TreePath path, Object selected, String odo) {
    LazyMutableTreeNode applicationNode = (LazyMutableTreeNode) selected;
    LazyMutableTreeNode projectNode = (LazyMutableTreeNode) applicationNode.getParent();
    CompletableFuture.runAsync(() -> {
      try {
        ExecHelper.execute(odo, "project", "set", projectNode.toString());
        ExecHelper.execute(odo, "app", "delete", "-f", applicationNode.toString());
        projectNode.remove(applicationNode);
      } catch (IOException e) {
        UIHelper.executeInUI(() -> JOptionPane.showMessageDialog(null, "Error: " + e.getLocalizedMessage(), "Delete application", JOptionPane.ERROR_MESSAGE));
      }
    });
  }
}
