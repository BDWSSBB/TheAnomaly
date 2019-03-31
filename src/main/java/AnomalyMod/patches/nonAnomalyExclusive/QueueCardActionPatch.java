package AnomalyMod.patches.nonAnomalyExclusive;

import AnomalyMod.AnomalyMod;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.actions.utility.QueueCardAction;
import com.megacrit.cardcrawl.cards.CardQueueItem;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import javassist.CannotCompileException;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;

// TODO: Fix things one by one (damn it)
//@SpirePatch(
//        clz = QueueCardAction.class,
//        method = "update"
//)
//public class QueueCardActionPatch {
//
//    public static ExprEditor Instrument() {
//        return new ExprEditor() {
//            @Override
//            public void edit(MethodCall m) throws CannotCompileException {
//                if (m.getMethodName().equals("add")) {
//                    m.replace(
//                            "{" +
//                                    Nested.class.getName() + ".theRightWay($$);" +
//                                    "if (false) {$_ = $proceed($$);}" +
//                                    "}");
//                }
//            }
//        };
//    }
//
//    public static class Nested {
//
//        public static void theRightWay(Object tmp) {
//            if (tmp instanceof CardQueueItem) {
//                AbstractDungeon.actionManager.cardQueue.add(0, (CardQueueItem) tmp);
//            }
//            else {
//                AnomalyMod.logger.info("WARNING: SOMETHING REALLY BAD HAPPENED IN PATCHING QueueCareAction.class. Please report to dev.");
//            }
//        }
//    }
//}
