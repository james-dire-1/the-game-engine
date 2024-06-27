package com.james.renderEngine.ui;

import com.james.tools.RenderingMath;
import com.james.renderEngine.ui.dataTypes.Size;
import org.lwjgl.util.vector.Vector2f;

public enum AnchorPoint implements Anchor {
    TOP {
        @Override
        public Vector2f anchor(Vector2f normalizedScale, Vector2f offset) {
            return RenderingMath.add(new Vector2f(0, 1), new Vector2f(0, -normalizedScale.y/2), offset);
        }
        @Override
        public Vector2f value(Size size) {
            return RenderingMath.add(new Vector2f(0, 1), new Vector2f(0, -size.normalized().y/2));
        }
        @Override
        public Vector2f valueWithParent(Size size, Gui parent) {
            float parentX = parent.position.normalized().x;
            float parentY = parent.position.normalized().y;
            float halfParentHeight = parent.size.normalized().y/2;
            return RenderingMath.add(new Vector2f(parentX, parentY + halfParentHeight), new Vector2f(0, -size.normalized().y/2));
        }
    },
    BOTTOM {
        @Override
        public Vector2f anchor(Vector2f normalizedScale, Vector2f offset) {
            return RenderingMath.add(new Vector2f(0, -1), new Vector2f(0, normalizedScale.y/2), offset);
        }
        @Override
        public Vector2f value(Size size) {
            return RenderingMath.add(new Vector2f(0, -1), new Vector2f(0, size.normalized().y/2));
        }
        @Override
        public Vector2f valueWithParent(Size size, Gui parent) {
            float parentX = parent.position.normalized().x;
            float parentY = parent.position.normalized().y;
            float halfParentHeight = parent.size.normalized().y/2;
            return RenderingMath.add(new Vector2f(parentX, parentY - halfParentHeight), new Vector2f(0, size.normalized().y/2));
        }
    },
    LEFT {
        @Override
        public Vector2f anchor(Vector2f normalizedScale, Vector2f offset) {
            return RenderingMath.add(new Vector2f(-1, 0), new Vector2f(normalizedScale.x/2, 0), offset);
        }
        @Override
        public Vector2f value(Size size) {
            return RenderingMath.add(new Vector2f(-1, 0), new Vector2f(size.normalized().x/2, 0));
        }
        @Override
        public Vector2f valueWithParent(Size size, Gui parent) {
            float parentX = parent.position.normalized().x;
            float parentY = parent.position.normalized().y;
            float halfParentWidth = parent.size.normalized().x/2;
            return RenderingMath.add(new Vector2f(parentX - halfParentWidth, parentY), new Vector2f(size.normalized().x/2, 0));
        }
    },
    RIGHT {
        @Override
        public Vector2f anchor(Vector2f normalizedScale, Vector2f offset) {
            return RenderingMath.add(new Vector2f(1, 0), new Vector2f(-normalizedScale.x/2, 0), offset);
        }
        @Override
        public Vector2f value(Size size) {
            return RenderingMath.add(new Vector2f(1, 0), new Vector2f(-size.normalized().x/2, 0));
        }
        @Override
        public Vector2f valueWithParent(Size size, Gui parent) {
            float parentX = parent.position.normalized().x;
            float parentY = parent.position.normalized().y;
            float halfParentWidth = parent.size.normalized().x/2;
            return RenderingMath.add(new Vector2f(parentX + halfParentWidth, parentY), new Vector2f(-size.normalized().x/2, 0));
        }
    },
    CENTER {
        @Override
        public Vector2f anchor(Vector2f normalizedScale, Vector2f offset) {
            return RenderingMath.add(new Vector2f(0, 0), offset);
        }
        @Override
        public Vector2f value(Size size) {
            return new Vector2f(0, 0);
        }
        @Override
        public Vector2f valueWithParent(Size size, Gui parent) {
            float parentX = parent.position.normalized().x;
            float parentY = parent.position.normalized().y;
            return new Vector2f(parentX, parentY);
        }
    },
    TOP_LEFT {
        @Override
        public Vector2f anchor(Vector2f normalizedScale, Vector2f offset) {
            return RenderingMath.add(new Vector2f(-1, 1), new Vector2f(normalizedScale.x/2, -normalizedScale.y/2), offset);
        }
        @Override
        public Vector2f value(Size size) {
            return RenderingMath.add(new Vector2f(-1, 1), new Vector2f(size.normalized().x/2, -size.normalized().y/2));
        }
        @Override
        public Vector2f valueWithParent(Size size, Gui parent) {
            float parentX = parent.position.normalized().x;
            float parentY = parent.position.normalized().y;
            float halfParentWidth = parent.size.normalized().x/2;
            float halfParentHeight = parent.size.normalized().y/2;
            return RenderingMath.add(new Vector2f(parentX - halfParentWidth, parentY + halfParentHeight),
                    new Vector2f(size.normalized().x/2,
                    -size.normalized().y/2));
        }
    },
    TOP_RIGHT {
        @Override
        public Vector2f anchor(Vector2f normalizedScale, Vector2f offset) {
            return RenderingMath.add(new Vector2f(1, 1), new Vector2f(-normalizedScale.x/2, -normalizedScale.y/2), offset);
        }
        @Override
        public Vector2f value(Size size) {
            return RenderingMath.add(new Vector2f(1, 1), new Vector2f(-size.normalized().x/2, -size.normalized().y/2));
        }
        @Override
        public Vector2f valueWithParent(Size size, Gui parent) {
            float parentX = parent.position.normalized().x;
            float parentY = parent.position.normalized().y;
            float halfParentWidth = parent.size.normalized().x/2;
            float halfParentHeight = parent.size.normalized().y/2;
            return RenderingMath.add(new Vector2f(parentX + halfParentWidth, parentY + halfParentHeight),
                    new Vector2f(-size.normalized().x/2,
                    -size.normalized().y/2));
        }
    },
    BOTTOM_LEFT {
        @Override
        public Vector2f anchor(Vector2f normalizedScale, Vector2f offset) {
            return RenderingMath.add(new Vector2f(-1, -1), new Vector2f(normalizedScale.x/2, normalizedScale.y/2), offset);
        }
        @Override
        public Vector2f value(Size size) {
            return RenderingMath.add(new Vector2f(-1, -1), new Vector2f(size.normalized().x/2, size.normalized().y/2));
        }
        @Override
        public Vector2f valueWithParent(Size size, Gui parent) {
            float parentX = parent.position.normalized().x;
            float parentY = parent.position.normalized().y;
            float halfParentWidth = parent.size.normalized().x/2;
            float halfParentHeight = parent.size.normalized().y/2;
            return RenderingMath.add(new Vector2f(parentX - halfParentWidth, parentY - halfParentHeight),
                    new Vector2f(size.normalized().x/2,
                    size.normalized().y/2));
        }
    },
    BOTTOM_RIGHT {
        @Override
        public Vector2f anchor(Vector2f normalizedScale, Vector2f offset) {
            return RenderingMath.add(new Vector2f(1, -1), new Vector2f(-normalizedScale.x/2, normalizedScale.y/2), offset);
        }
        @Override
        public Vector2f value(Size size) {
            return RenderingMath.add(new Vector2f(1, -1), new Vector2f(-size.normalized().x/2, size.normalized().y/2));
        }
        @Override
        public Vector2f valueWithParent(Size size, Gui parent) {
            float parentX = parent.position.normalized().x;
            float parentY = parent.position.normalized().y;
            float halfParentWidth = parent.size.normalized().x/2;
            float halfParentHeight = parent.size.normalized().y/2;
            return RenderingMath.add(new Vector2f(parentX + halfParentWidth, parentY - halfParentHeight),
                    new Vector2f(-size.normalized().x/2,
                    size.normalized().y/2));
        }
    }
}
